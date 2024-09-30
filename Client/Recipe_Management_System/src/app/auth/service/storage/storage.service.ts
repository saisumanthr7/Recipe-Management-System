import { Injectable } from '@angular/core';

const TOKEN = 'token';
const USER = 'user';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  constructor() { }

  // Removes the existing token and save the new token through localstorage
  static saveToken(token: string): void {
    window.localStorage.removeItem(TOKEN);
    window.localStorage.setItem(TOKEN, token);
  }


  // Retreives the user the user using local storage. And parses the stored user into a json
  static getUser(){
    var storedUser = localStorage.getItem(USER);
    if(storedUser !== null){
      return JSON.parse(storedUser);
    }else{
      return null;
    }
  }

  // Retrieves the user id from the existing user
  static getUserId(): string {
    const user = this.getUser();
    if(user == null) { return '';}
    return user.id;
  }

  // Removes the existing user and adds the new user using local storage
  static saveUser(user: any):void {
    window.localStorage.removeItem(USER);
    window.localStorage.setItem(USER, JSON.stringify(user));
  }

  // Retrieve the token from local storage
  static getToken(){
    return window.localStorage.getItem(TOKEN)
  }

  // Retrieve the user role using the getUser method
  static getUserRole(): string {
    const user = this.getUser();
    if(user == null) return "";
    return user.role;
  }

  static isAdminLoggedIn(): boolean{
    if(this.getToken() == null) return false;
    const role: string = this.getUserRole();
    return role == "ADMIN";
  }

  static isCustomerLoggedIn(): boolean {
    if(this.getToken() == null) return false;
    const role: string = this.getUserRole();
    return role == "CUSTOMER";
  }

  static logout(): void {
    window.localStorage.removeItem(TOKEN);
    window.localStorage.removeItem(USER);
  }
}
