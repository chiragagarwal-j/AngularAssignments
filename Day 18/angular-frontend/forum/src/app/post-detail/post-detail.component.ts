import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostService } from '../post.service';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.css']
})
export class PostDetailComponent implements OnInit {
  post: any;
  postid?: number;

  constructor(private postService: PostService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const postId = +params['id'];
      if (!isNaN(postId)) {
        this.postid = postId;
        this.loadPost(postId);
      }
    });
  }

  loadPost(postId: number) {
    this.postService.getPost(postId).subscribe((data: any) => {
      this.post = data;
    });
  }
}