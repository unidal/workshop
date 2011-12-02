package org.unidal.signin;

public interface ITokenBuilder<C extends IContext, T extends IToken> {
	public T parse(C ctx, String str);

	public String build(C ctx, T token);
}
