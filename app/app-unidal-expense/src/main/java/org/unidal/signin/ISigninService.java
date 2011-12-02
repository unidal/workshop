package org.unidal.signin;

public interface ISigninService<C extends IContext, T extends ICredential, S extends ISession> {
	public S signin(C ctx, T credential);

	public void signout(C ctx);

	public S validate(C ctx);
}
