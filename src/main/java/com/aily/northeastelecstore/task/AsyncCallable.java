package com.aily.northeastelecstore.task;



public interface AsyncCallable<T> {
	public void call(final Callback<T> pCallback, final Callback<Exception> pExceptionCallback);
}
