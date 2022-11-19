export class PasswordChangeInfo {
  currentPassword!: string;
  newPassword!: string;
  reEnteredNewPassword!: string;

  constructor(currentPassword: string, newPassword: string, reEnteredNewPassword: string) {
    this.currentPassword = currentPassword;
    this.newPassword = newPassword;
    this.reEnteredNewPassword = reEnteredNewPassword;
  }
}
