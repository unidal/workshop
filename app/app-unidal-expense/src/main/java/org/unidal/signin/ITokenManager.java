package org.unidal.signin;

public interface ITokenManager<C extends IContext, T extends IToken> {
	public T getToken(C ctx, String name);

	public void removeToken(C ctx, String name);

	public void setToken(C ctx, T token);
}