import {State, Action, StateContext, Selector} from "@ngxs/store";
import {DecrementTokens, SetTokens} from "../actions/tokens.action";

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

  @Action(DecrementTokens)
  decrementTokens({setState, getState}: StateContext<TokensStateModel>, action: DecrementTokens) {
    const counter = getState();
    counter.numberOfTokens = counter.numberOfTokens - action.payload
    setState({numberOfTokens: counter.numberOfTokens});
  }

  @Selector()
  static value(state: TokensStateModel) {
    return state.numberOfTokens
  }

}
