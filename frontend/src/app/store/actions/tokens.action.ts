export class SetTokens {
  static readonly type = '[tokes] set tokens';
  constructor(public payload: number) {
  }
}

export class DecrementTokens {
  static readonly type = '[tokes] decrement tokens';
  constructor(public payload: number) {
  }
}


