package com.Rest.forum.controller.poststats;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Rest.forum.controller.binding.AddCommentForm;
import com.Rest.forum.controller.binding.AddPostForm;
import com.Rest.forum.entity.Comment;
import com.Rest.forum.entity.LikeId;
import com.Rest.forum.entity.LikeRecord;
import com.Rest.forum.entity.Post;
import com.Rest.forum.entity.User;
import com.Rest.forum.model.RegistrationForm;
import com.Rest.forum.repository.CommentRepository;
import com.Rest.forum.repository.LikeCRUDRepository;
import com.Rest.forum.repository.PostRepository;
import com.Rest.forum.repository.UserRepository;
import com.Rest.forum.service.DomainUserService;
import com.Rest.forum.service.PostService;
import com.Rest.forum.service.TaskDefinitionBean;
import com.Rest.forum.service.TaskSchedulingService;
import com.Rest.forum.service.UserService;
import com.Rest.forum.utils.CronUtil;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;

@RestController
@CrossOrigin
@RequestMapping("/forum")
public class ForumController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private DomainUserService domainUserService;

	@Autowired
	private LikeCRUDRepository likeCRUDRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private TaskSchedulingService taskSchedulingService;

	@Autowired
	private CronUtil cronUtil;

	@Autowired
	private PostService postService;

	@Autowired
	private UserService userService;

	@PostConstruct
	public void init() {
	}

	@GetMapping("/post/form")
	public String getPostForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		AddPostForm postForm = new AddPostForm();
		User author = domainUserService.getByName(userDetails.getUsername()).get();
		postForm.setUserId(author.getId());
		model.addAttribute("postForm", postForm);
		return "forum/postForm";
	}

	@PostMapping("/post/add")
	public String addNewPost(@ModelAttribute("postForm") AddPostForm postForm, BindingResult bindingResult,
			RedirectAttributes attr) throws ServletException, ParseException {
		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult.getFieldErrors());
			attr.addFlashAttribute("org.springframework.validation.BindingResult.post", bindingResult);
			attr.addFlashAttribute("post", postForm);
			return "redirect:/forum/post/form";
		}

		Optional<User> user = userRepository.findById(postForm.getUserId());
		if (user.isEmpty()) {
			throw new ServletException("Something went seriously wrong, and we couldn't find the user in the DB");
		}

		Post post = new Post();
		post.setAuthor(user.get());
		post.setContent(postForm.getContent());
		if (postForm.getScheduleDate() != null) {
			String cronExpression = cronUtil.dateToCronExpression(postForm.getScheduleDate().toString());
			String jobId = "post_" + UUID.randomUUID().toString();
			TaskDefinitionBean taskBean = new TaskDefinitionBean(post, postRepository);
			taskSchedulingService.scheduleATask(jobId, taskBean, cronExpression);
		} else {
			postRepository.save(post);
		}
		return String.format("redirect:/forum/mypost");
	}

	@GetMapping("/mypost")
	public String MyPostList(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		List<Post> postList;
		Optional<User> user = userRepository.findByName(userDetails.getUsername());
		postList = postService.getMyPosts(user.get().getId());
		model.addAttribute("posts", postList);
		return "forum/myPosts";
	}

	@PostMapping("/post/{id}/delete")
	public String deletePost(@PathVariable Integer id,
			@RequestParam(name = "scheduleDate", required = false) LocalDateTime dateTime)
			throws ParseException {
		if (dateTime == null) {
			postService.deleteLikeAndComment(id);
			postService.deletePostById(id);
		} else {
			String cronExpression = cronUtil.dateToCronExpression(dateTime.toString());
			String jobId = "post_" + UUID.randomUUID().toString();
			TaskDefinitionBean taskBean = new TaskDefinitionBean(id, postService);
			taskSchedulingService.scheduleATask(jobId, taskBean, cronExpression);
		}

		return "redirect:/forum/mypost";
	}

	@GetMapping("/post/{id}/edit")
	public String editPost(@PathVariable Integer id, Model model, @AuthenticationPrincipal UserDetails userDetails) {

		AddPostForm postForm = new AddPostForm();
		User author = domainUserService.getByName(userDetails.getUsername()).get();
		postForm.setUserId(author.getId());
		Optional<Post> post;
		post = postRepository.findById(id);
		postForm.setUserId(post.get().getId());
		postForm.setContent(post.get().getContent());
		model.addAttribute("postForm", postForm);
		model.addAttribute("postId", id);

		return "forum/editForm";
	}

	@PostMapping("/post/{id}/edit/save")
	public String editPostSave(@RequestParam("postId") Integer id, @ModelAttribute("postForm") AddPostForm postForm,
			BindingResult bindingResult,
			RedirectAttributes attr) throws ServletException {
		postRepository.updatePost(id, postForm.getContent());
		return String.format("redirect:/forum/mypost");
	}

	@GetMapping("/post/{id}")
	public ResponseEntity<Post> getPostDetail(@PathVariable int id) {
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No post with the requested ID"));
		return new ResponseEntity<Post>(post, HttpStatus.OK);
	}

	@PostMapping("/post/{postId}/add-comment")
	public ResponseEntity<Comment> addCommentToPost(@PathVariable int postId, @RequestBody AddCommentForm addComment) {
		Optional<Post> postOptional = postRepository.findById(postId);
		if (postOptional.isPresent()) {
			Post post = postOptional.get();

			Comment comment = new Comment();
			comment.setContent(addComment.getContent());
			comment.setPost(post);
			User user = userService.findUserByName("chirag").orElse(null);
			if (post != null) {
				comment.setUser(user);
				commentRepository.save(comment);
				return new ResponseEntity<>(comment, HttpStatus.CREATED);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/post/{postId}/comments")
	public ResponseEntity<List<Comment>> getCommentsForPost(@PathVariable int postId) {
		List<Comment> comments = commentRepository.findAllByPostId(postId);
		if (comments.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}

	@GetMapping("/getUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

	// @GetMapping("/post/{id}")
	// public String postDetail(@PathVariable int id, Model model,
	// @AuthenticationPrincipal UserDetails userDetails)
	// throws ResourceNotFoundException {
	// Optional<Post> post = postRepository.findById(id);
	// if (post.isEmpty()) {
	// throw new ResourceNotFoundException("No post with the requested ID");
	// }
	// model.addAttribute("post", post.get());

	// List<Comment> commentList = commentRepository.findAllByPostId(id);
	// model.addAttribute("commentList", commentList);

	// model.addAttribute("likerName", userDetails.getUsername());
	// int numLikes = likeCRUDRepository.countByLikeIdPost(post.get());
	// model.addAttribute("likeCount", numLikes);

	// model.addAttribute("commentForm", new AddCommentForm());
	// return "forum/postDetail";
	// }

	@PostMapping("/post/{id}/like")
	public String postLike(@PathVariable int id, String likerName, RedirectAttributes attr) {
		LikeId likeId = new LikeId();
		likeId.setUser(userRepository.findByName(likerName).get());
		likeId.setPost(postRepository.findById(id).get());
		LikeRecord like = new LikeRecord();
		like.setLikeId(likeId);
		likeCRUDRepository.save(like);
		return String.format("redirect:/forum/post/%d", id);
	}

	@PostMapping("/post/{id}/comment")
	public String addCommentToPost(@ModelAttribute("commentForm") AddCommentForm commentForm, @PathVariable int id,
			@AuthenticationPrincipal UserDetails userDetails) {
		Optional<Post> post = postRepository.findById(id);
		if (post.isPresent()) {
			Comment comment = new Comment();
			comment.setContent(commentForm.getContent());
			comment.setPost(post.get());
			comment.setUser(domainUserService.getByName(userDetails.getUsername()).get());
			commentRepository.save(comment);
		}
		return String.format("redirect:/forum/post/%d", id);
	}

	@GetMapping("/register")
	public String getRegistrationForm(Model model) {
		if (!model.containsAttribute("registrationForm")) {
			model.addAttribute("registrationForm", new RegistrationForm());
		}
		return "forum/register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute("registrationForm") RegistrationForm registrationForm,
			BindingResult bindingResult,
			RedirectAttributes attr) {
		if (bindingResult.hasErrors()) {
			attr.addFlashAttribute("org.springframework.validation.BindingResult.registrationForm", bindingResult);
			attr.addFlashAttribute("registrationForm", registrationForm);
			return "redirect:/register";
		}
		if (!registrationForm.isValid()) {
			attr.addFlashAttribute("message", "Passwords must match");
			attr.addFlashAttribute("registrationForm", registrationForm);
			return "redirect:/register";
		}
		domainUserService.save(registrationForm.getUsername(), registrationForm.getPassword());
		attr.addFlashAttribute("result", "Registration success!");
		return "redirect:/login";
	}

}
