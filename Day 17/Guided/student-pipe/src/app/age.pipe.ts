import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'age',
})
export class AgePipe implements PipeTransform {
  transform(dob: string): string {
    const birthDate = new Date(dob);
    const today = new Date();

    const years = today.getFullYear() - birthDate.getFullYear();
    const birthMonth = birthDate.getMonth();
    const currentMonth = today.getMonth();

    if (currentMonth < birthMonth || (currentMonth === birthMonth && today.getDate() < birthDate.getDate())) {
      return (years - 1).toString();
    }

    return years.toString();
  }
}