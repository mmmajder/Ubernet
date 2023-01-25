export class ReservedRideItem {
  start: string;
  destination: string;
  time: string;
  friends: FriendDto[];
  timeLeft: string;
}

export class FriendDto {
  name = '';
  photo = '../assets/default-profile-picture.jpg'
}
