package scale;


public interface Scaleable {
	public void operation(int operationNumber, String[] inputArguments);

	public void operationSynchronous(int operationNumber, String[] inputArguments);
}