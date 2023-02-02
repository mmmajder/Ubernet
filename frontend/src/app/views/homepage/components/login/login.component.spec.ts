import {TestBed, ComponentFixture} from '@angular/core/testing';
import {AuthService} from "../../../../services/auth.service";
import {UserRole} from "../../../../model/UserRole";
import {LoginResponseDto} from "../../../../model/LoginResponseDto";
import {HomepageModule} from "../../homepage.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {DebugElement} from "@angular/core";
import {By} from '@angular/platform-browser';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {LoginComponent} from "./login.component";
import {StateStream, Store} from "@ngxs/store";
import {InternalStateOperations} from "@ngxs/store/src/internal/state-operations";
import {SocialAuthService, SocialLoginModule} from "@abacritt/angularx-social-login";

describe('ContactComponent', () => {
  let comp: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let elem: DebugElement;
  let submitButton: HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HomepageModule,
        ReactiveFormsModule,
        FormsModule,
        BrowserAnimationsModule
      ], declarations: [
        LoginComponent
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    const loginResponseDto: LoginResponseDto = {
      token: {
        accessToken: 'iuwebguiewbgew',
        expiresIn: 6000
      },
      userRole: UserRole.CUSTOMER
    };

    const authServiceSpy = jasmine.createSpyObj('AuthService', ['login']);
    const storeSpy = jasmine.createSpyObj('Store', ['dispatch']);
    const socialSpy = jasmine.createSpyObj('SocialAuthService', ['signIn']);
    authServiceSpy.login.and.returnValue(loginResponseDto);

    TestBed.configureTestingModule({
      declarations: [
        LoginComponent
      ],
      providers: [
        {provide: AuthService, useValue: authServiceSpy},
        {provide: Store, useValue: storeSpy},
        {provide: SocialAuthService, useValue: socialSpy},
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    comp = fixture.componentInstance; // RegistrationComponent test instance
    elem = fixture.debugElement.query(By.css('#login-button'));
    submitButton = elem.nativeElement;
    let store = fixture.debugElement.injector.get(Store);
    fixture.detectChanges();
  });

  it(`should load login form`, () => {
    const cardTitle = document.getElementsByTagName('mat-card-title');
    expect(cardTitle[0].textContent).toEqual("Welcome back...");
  });

  it(`form should be valid`, () => {
    comp.formGroup.controls['emailFormControl'].setValue("pera@gmail.com");
    comp.formGroup.controls['passwordFormControl'].setValue("pera123");

    fixture.detectChanges();
    expect((submitButton as HTMLButtonElement).disabled).toBeFalsy();
  });

  it('should call login method when button is clicked', () => {
    spyOn(comp, 'login');
    comp.formGroup.controls['emailFormControl'].setValue("pera@gmail.com");
    comp.formGroup.controls['passwordFormControl'].setValue("pera123");

    fixture.detectChanges();
    (submitButton as HTMLButtonElement).click();
    expect(comp.login).toHaveBeenCalledTimes(1);
  });

  it('should not call login method when button is clicked if invalid form', () => {
    spyOn(comp, 'login');
    comp.formGroup.controls['emailFormControl'].setValue("peracom");
    comp.formGroup.controls['passwordFormControl'].setValue("pera123");

    fixture.detectChanges();
    (submitButton as HTMLButtonElement).click();
    expect(comp.login).toHaveBeenCalledTimes(0);
  });

  it(`form should be invalid - empty email`, () => {
    comp.formGroup.controls['emailFormControl'].setValue("");

    fixture.detectChanges();
    expect((submitButton as HTMLButtonElement).disabled).toBeTruthy();
    expect(comp.formGroup.hasError('required', 'emailFormControl')).toBeTruthy();
  });

  it(`form should be invalid - wrong email password`, () => {
    comp.formGroup.controls['emailFormControl'].setValue("pera.");

    fixture.detectChanges();
    expect((submitButton as HTMLButtonElement).disabled).toBeTruthy();
    expect(comp.formGroup.hasError('email', 'emailFormControl')).toBeTruthy();
  });

});
