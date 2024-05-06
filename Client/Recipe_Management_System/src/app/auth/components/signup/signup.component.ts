import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {

  signupForm!: FormGroup;

  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.signupForm = this.fb.group({
      firstName: [null, [Validators.required]],
      lastName: [null, [Validators.required]],
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required, Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)]],
      confirmPassword: [null, [Validators.required, this.confirmPasswordValidator]]
    })
  }

  // passwordValidation(control: FormControl): { [s: string]: boolean} {
  //   const password = control.value;
  //   const regex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$/;
    
  //   if (!password) {
  //     return { required: true };
  //   } else if (!regex.test(password)) {
  //     return { pattern: true };
  //   }
    
  //   return {};
  // }

  confirmPasswordValidator(control: FormControl): { [s: string]: boolean } | null {
    const password = control.root.get('password');
    const confirmPassword = control.value;
    if (password && confirmPassword !== password.value) {
      return { 'confirm': true };
    }
    return null;
  }

  register(){
    // console.log(this.signupForm.value);
    if(this.signupForm.valid){
      this.authService.register(this.signupForm.value).subscribe((res) =>{
        console.log(res);
        if(res.access_token != null){
          alert("Signup is Successful");
          this.router.navigateByUrl("/login");
        }else if(res.errorMessage){
          alert("Email already exists!");
        }
        else {
          alert("Something went wrong! Please try again!")
        }
      }); 
    }else {
      alert("Plese fill all the required fiels correctly.")
    }
  }
}
