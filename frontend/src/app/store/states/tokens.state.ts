import {State, Action, StateContext, Selector} from "@ngxs/store";
import {SetTokens} from "../actions/tokens.action";

export interface TokensStateModel {
  numberOfTokens: number
}

@State<TokensStateModel>({
  name: 'tokens',
  defaults: {
    numberOfTokens: 0
  }
})
export class TokensState {
  @Action(SetTokens)
  setTokens({patchState}: StateContext<TokensStateModel>, {payload}: SetTokens) {
    patchState({numberOfTokens: payload})
  }

  @Selector()
  static value(state: TokensStateModel) {
    return state.numberOfTokens
  }

}
