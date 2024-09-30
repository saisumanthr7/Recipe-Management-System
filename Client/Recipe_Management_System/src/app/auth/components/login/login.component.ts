import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth/auth.service';
import { Router } from '@angular/router';
import { StorageService } from '../../service/storage/storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  loginForm!: FormGroup;

  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loginForm = this.fb.group({
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]]
    })
  }

  login() {
    // console.log(this.loginForm.value);
    if(this.loginForm.valid){
      this.authService.login(this.loginForm.value).subscribe((res) => {
        if(res.id != null){
          const user = {
            id: res.id,
            role: res.role
          }
          StorageService.saveUser(user);
          StorageService.saveToken(res.jwt);
          if(StorageService.isAdminLoggedIn()){
            this.router.navigateByUrl("/admin/dashboard");
          }else if(StorageService.isCustomerLoggedIn()){
            this.router.navigateByUrl("/customer/dashboard")
          }else{
            alert("Bad Credentials")
          }
        }
      })
    }
  }
}
