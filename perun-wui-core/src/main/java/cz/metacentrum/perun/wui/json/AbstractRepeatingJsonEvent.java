package cz.metacentrum.perun.wui.json;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.model.PerunException;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 	 JsonEvent class that expects to be used multiple times.
 * </p>
 * <p>
 * 	 When is used for the X-th time specified by value in constructor,
 * it calls method 'done'. When receives some error, it calls the method err.
 * But it can maintain another JsonCalls, but if someError happens, it does not call the erred method again.
 * </p>
 * <p>
 * 	 Also, before first call it calls the method started.
 * </p>
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public abstract class AbstractRepeatingJsonEvent implements RepeatingJsonEvent {

	private List<JavaScriptObject> results = new ArrayList<>();

	private int counter;

	private int repeatTimes;

	private boolean finished = false;

	private boolean erred = false;

	public AbstractRepeatingJsonEvent(int repeatTimes) {
		if(repeatTimes <= 0) {
			throw new IllegalArgumentException("RepeatTimes must be at least 1.");
		}
		this.repeatTimes = repeatTimes;

		this.counter = 0;
	}

	@Override
	public void onFinished(JavaScriptObject result) {
		if (finished) {
			throw new IllegalStateException("Event called more times than specified in constructor");
		}

		results.add(result);
		counter++;

		if (counter == repeatTimes) {
			finished = true;
			done(results);
		}
	}

	@Override
	public void onError(PerunException error) {
		counter++;

		if (!erred) {
			erred(error);
			erred = true;
		}
	}

	@Override
	public void onLoadingStart() {
		if (counter == 0) {
			started();
		}
	}
}
