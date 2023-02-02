import {TestBed, ComponentFixture} from '@angular/core/testing';
import {RegistrationComponent} from "./registration.component";
import {AuthService} from "../../../../services/auth.service";
import {HomepageModule} from "../../homepage.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {DebugElement} from "@angular/core";
import {By} from '@angular/platform-browser';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

describe('ContactComponent', () => {
  let comp: RegistrationComponent;
  let fixture: ComponentFixture<RegistrationComponent>;
  let elem: DebugElement;
  let submitButton: HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HomepageModule,
        ReactiveFormsModule,
        FormsModule,
        BrowserAnimationsModule
      ],
      declarations: [
        RegistrationComponent
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['register']);

    TestBed.configureTestingModule({
      declarations: [
        RegistrationComponent
      ],
      providers: [
        {provide: AuthService, useValue: authServiceSpy}
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegistrationComponent);
    comp = fixture.componentInstance; // RegistrationComponent test instance
    elem = fixture.debugElement.query(By.css('#register-button'));
    submitButton = elem.nativeElement;

    fixture.detectChanges();
  });

  it(`should load registration form`, () => {
    const cardTitle = document.getElementsByTagName('mat-card-title');
    expect(cardTitle[0].textContent).toEqual("Sign up");
  });

  it(`form should be valid`, () => {
    comp.formGroup.controls['emailFormControl'].setValue("pera@gmail.com");
    comp.formGroup.controls['lastNameFormControl'].setValue("Peric");
    comp.formGroup.controls['nameFormControl'].setValue("Pera");
    comp.formGroup.controls['passwordFormControl'].setValue("Pera123");
    comp.formGroup.controls['password2FormControl'].setValue("Pera123");
    comp.formGroup.controls['phoneFormControl'].setValue("069123456");
    comp.formGroup.controls['cityFormControl'].setValue("Novi Sad");

    fixture.detectChanges();
    expect(comp.formGroup.valid).toBeTruthy();
    expect((submitButton as HTMLButtonElement).disabled).toBeFalsy();
  });

  it('should call register method when button is clicked', () => {
    spyOn(comp, 'registerNewUser');
    comp.formGroup.controls['emailFormControl'].setValue("pera@gmail.com");
    comp.formGroup.controls['lastNameFormControl'].setValue("Peric");
    comp.formGroup.controls['nameFormControl'].setValue("Pera");
    comp.formGroup.controls['passwordFormControl'].setValue("Pera123");
    comp.formGroup.controls['password2FormControl'].setValue("Pera123");
    comp.formGroup.controls['phoneFormControl'].setValue("069123456");
    comp.formGroup.controls['cityFormControl'].setValue("Novi Sad");

    fixture.detectChanges();
    (submitButton as HTMLButtonElement).click();
    expect(comp.registerNewUser).toHaveBeenCalledTimes(1);
  });

  it('should not call register method when button is clicked if invalid form', () => {
    spyOn(comp, 'registerNewUser');
    comp.formGroup.controls['emailFormControl'].setValue("pera");

    fixture.detectChanges();
    (submitButton as HTMLButtonElement).click();
    expect(comp.registerNewUser).toHaveBeenCalledTimes(0);
  });

  it(`form should be invalid - empty phone number`, () => {
    comp.formGroup.controls['emailFormControl'].setValue("pera@gmail.com");
    comp.formGroup.controls['lastNameFormControl'].setValue("Peric");
    comp.formGroup.controls['nameFormControl'].setValue("Pera");
    comp.formGroup.controls['passwordFormControl'].setValue("Pera123");
    comp.formGroup.controls['password2FormControl'].setValue("Pera123");
    comp.formGroup.controls['phoneFormControl'].setValue("");
    comp.formGroup.controls['cityFormControl'].setValue("Novi Sad");

    fixture.detectChanges();
    expect(comp.formGroup.valid).toBeFalsy();
    expect((submitButton as HTMLButtonElement).disabled).toBeTruthy();
    expect(comp.formGroup.hasError('required', 'phoneFormControl')).toBeTruthy();
  });

  it(`form should be invalid - email wrong format`, () => {
    comp.formGroup.controls['emailFormControl'].setValue("pera");
    comp.formGroup.controls['lastNameFormControl'].setValue("Peric");
    comp.formGroup.controls['nameFormControl'].setValue("Pera");
    comp.formGroup.controls['passwordFormControl'].setValue("Pera123");
    comp.formGroup.controls['password2FormControl'].setValue("Pera123");
    comp.formGroup.controls['phoneFormControl'].setValue("069123456");
    comp.formGroup.controls['cityFormControl'].setValue("Novi Sad");

    fixture.detectChanges();
    expect(comp.formGroup.valid).toBeFalsy();
    expect((submitButton as HTMLButtonElement).disabled).toBeTruthy();
    expect(comp.formGroup.hasError('email', 'emailFormControl')).toBeTruthy();
  });

  it(`form should be invalid - short password`, () => {
    comp.formGroup.controls['emailFormControl'].setValue("pera@gmail.com");
    comp.formGroup.controls['lastNameFormControl'].setValue("Peric");
    comp.formGroup.controls['nameFormControl'].setValue("Pera");
    comp.formGroup.controls['passwordFormControl'].setValue("Pera");
    comp.formGroup.controls['password2FormControl'].setValue("Pera");
    comp.formGroup.controls['phoneFormControl'].setValue("069123456");
    comp.formGroup.controls['cityFormControl'].setValue("Novi Sad");

    fixture.detectChanges();
    expect(comp.formGroup.valid).toBeFalsy();
    expect((submitButton as HTMLButtonElement).disabled).toBeTruthy();
    expect(comp.formGroup.hasError('minlength', 'passwordFormControl')).toBeTruthy();
  });
});
