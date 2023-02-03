import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import {AuthService} from './auth.service';
import {TestBed} from "@angular/core/testing";
import {
  mockAlreadyExistingUserRegistrationData,
  mockInvalidLoginCredentials,
  mockInvalidRegistrationData,
  mockValidLoginCredentials,
  mockValidLoginResponse, mockValidRegistrationData
} from "./auth.service.mock";

describe('AuthService', () => {
  let service: AuthService;
  let httpController: HttpTestingController;

  let authUrl = 'http://localhost:8000/auth';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(AuthService);
    httpController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpController.verify();
  });

  it('authService should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return expected login response (HttpClient called once)', () => {
    service.login(mockValidLoginCredentials).subscribe((data) => {
      expect(data).toEqual(mockValidLoginResponse);
    });

    httpController.expectOne({
      method: 'POST',
      url: authUrl + "/login"
    }).flush(mockValidLoginResponse);
  });

  it('invalid login for not existing or not verified users', () => {
    service.login(mockInvalidLoginCredentials).subscribe({
      next: (data) => {
        console.log(data)
        fail('Expected 404 but got response');
      },
      error: (err) => {
        expect(err.error.message).toEqual('Wrong email or password');
      },
    });

    httpController.expectOne({
      method: 'POST',
      url: authUrl + "/login"
    }).flush(
      {message: 'Wrong email or password'},
      {status: 404, statusText: 'Not found'}
    );
  });

  it('invalid registartion - user already exist', () => {
    service.register(mockAlreadyExistingUserRegistrationData).subscribe({
      next: () => {
        fail('Expected 400 but got response');
      },
      error: (err) => {
        expect(err.error.message).toEqual('User with this email already exist');
      },
    });

    httpController.expectOne({
      method: 'POST',
      url: authUrl + "/register"
    }).flush(
      {message: 'User with this email already exist'},
      {status: 400, statusText: 'Bad request'}
    );
  });

  it('invalid registartion - invalid data', () => {
    service.register(mockInvalidRegistrationData).subscribe({
      next: () => {
        fail('Expected 400 but got response');
      },
      error: (err) => {
        expect(err.error.message).toEqual('Email is in invalid format');
      },
    });

    httpController.expectOne({
      method: 'POST',
      url: authUrl + "/register"
    }).flush(
      {message: 'Email is in invalid format'},
      {status: 400, statusText: 'Bad request'}
    );
  });

  it('valid registartion', () => {
    service.register(mockValidRegistrationData).subscribe({
      next: (data) => expect(data).toEqual("We sent you registration link"),
      error: () => fail("Request should be valid")
    });

    httpController.expectOne({
      method: 'POST',
      url: authUrl + "/register"
    }).flush("We sent you registration link");
  });
})
