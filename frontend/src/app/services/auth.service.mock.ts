import {LoginCredentials} from "../model/LoginCredentials";
import {LoginResponseDto} from "../model/LoginResponseDto";
import {UserRole} from "../model/UserRole";
import {RegisterCredentials} from "../model/RegisterCredentials";

export const mockValidLoginCredentials: LoginCredentials = {
  email: "driver@gmail.com",
  password: "driver"
};

export const mockInvalidLoginCredentials: LoginCredentials = {
  email: "jksdbef",
  password: ""
};

export const mockValidLoginResponse: LoginResponseDto = {
  userRole: UserRole.DRIVER,
  token: {
    accessToken: "akgbaugkb",
    expiresIn: 10000
  }
}

export const mockInvalidRegistrationData: RegisterCredentials = {
  email: "pera",
  password: "pera123",
  phoneNumber: "06896967",
  name: "Pera",
  surname: "Peric",
  city: "Novi Sad"
};

export const mockValidRegistrationData: RegisterCredentials = {
  email: "pera",
  password: "pera123",
  phoneNumber: "06896967",
  name: "Pera",
  surname: "Peric",
  city: "Novi Sad"
};

export const mockAlreadyExistingUserRegistrationData: RegisterCredentials = {
  email: "customer@gmail.com",
  password: "pera123",
  phoneNumber: "06896967",
  name: "Pera",
  surname: "Peric",
  city: "Novi Sad"
}
