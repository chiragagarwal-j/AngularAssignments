import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostService } from '../post.service';
import { User, UserService } from '../UserService';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.css']
})
export class PostDetailComponent implements OnInit {
  post: any;
  postid?: number;
  commentForm: { content: string } = { content: '' };
  comments: any[] = [];
  users: any[] = [];
  suggestionsFetched: boolean = false;
  showUserSuggestions = false;
  userSuggestions: User[] = [];
  commentFormg: FormGroup = new FormGroup({});

  constructor(private postService: PostService, private route: ActivatedRoute , private userservice : UserService,private fb: FormBuilder) { 
    this.commentFormg = this.fb.group({

      commentText: ['', Validators.required]

    });

  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const postId = +params['id'];
      if (!isNaN(postId)) {
        this.postid = postId;
        this.loadPost(postId);
        this.loadComments(postId);
      }
    });
  }

  loadPost(postId: number) {
    this.postService.getPost(postId).subscribe((data: any) => {
      this.post = data;
    });
  }

  addComment() {
    if (this.postid !== undefined) {
      const newComment = {
        content: this.commentForm.content
      };

      this.postService.addComment(this.postid, newComment).subscribe((response: any) => {
        this.loadPost(this.postid!);
        this.commentForm.content = '';
      });
    } else {
      console.error('Post ID is undefined.');
    }
  }

  loadComments(postId: number) {
    this.postService.getCommentsForPost(postId).subscribe((data: any[]) => {
      this.comments = data;
    });
  }

  fetchUserSuggestions(usernameInitial: string) {
    this.userservice.getUsers().subscribe(users => {
      this.userSuggestions = users.filter(user => {
        return user.name.toLowerCase().startsWith(usernameInitial.toLowerCase());

      });
      this.suggestionsFetched = true;
      this.showUserSuggestions = this.userSuggestions.length > 0;
    });
  }

  onCommentInputChange(event: Event) {
    const inputValue = (event.target as HTMLInputElement).value;
    if (inputValue.includes('@')) {
      const usernamePartial = inputValue.split('@').slice(-1)[0].trim();
      if (!this.suggestionsFetched && usernamePartial.length > 0) {
        this.fetchUserSuggestions(usernamePartial);
      } else {
        this.showUserSuggestions = this.userSuggestions.length > 0;
      }
    } else {
      this.showUserSuggestions = false;
    }
  }

  insertSuggestion(user: User) {
    const commentTextControl = this.commentFormg.get('commentText');
    if (commentTextControl) {
      const commentText = commentTextControl.value;
      const username = user.name;
      if (!commentText.match(/\B@[\w\s]*$/)) {
        commentTextControl.setValue(`${commentText ? commentText + ' ' : ''}@${username} `);
      } else {
        const updatedCommentText = commentText.replace(/\B@[\w\s]*$/, `@${username} `);
        commentTextControl.setValue(updatedCommentText);
      }
    }
  }
}
