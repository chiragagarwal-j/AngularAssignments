import { Component } from '@angular/core';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css'],
})
export class StudentComponent {
  students = [
    {
      Sid: 1,

      Sname: 'A',

      dob: '2001-05-15',

      course: 'CSE',

      fees: 2000,
    },

    {
      Sid: 2,

      Sname: 'B',

      dob: '2003-08-25',

      course: 'ECE',

      fees: 2000,
    },

    {
      Sid: 3,

      Sname: 'C',

      dob: '2002-04-10',

      course: 'EEE',

      fees: 2000,
    },

    {
      Sid: 4,

      Sname: 'D',

      dob: '2001-12-05',

      course: 'IT',

      fees: 2000,
    },
  ];
}
