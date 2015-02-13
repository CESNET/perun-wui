package cz.metacentrum.perun.wui.widgets;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.base.ValueBoxBase;
import org.gwtbootstrap3.client.ui.constants.Styles;

/**
 * Extension of GwtBootstrap3 SuggestBox to replace suggestions by default GWT implementation.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class SuggestBox extends org.gwtbootstrap3.client.ui.SuggestBox {

	/**
	 * Constructor for {@link SuggestBox}. Creates a {@link com.google.gwt.user.client.ui.MultiWordSuggestOracle} and {@link org.gwtbootstrap3.client.ui.TextBox} to use
	 * with this {@link SuggestBox}.
	 */
	public SuggestBox() {
		this(new MultiWordSuggestOracle());
	}

	/**
	 * Constructor for {@link SuggestBox}. Creates a {@link org.gwtbootstrap3.client.ui.TextBox} to use with this {@link SuggestBox}.
	 *
	 * @param oracle the oracle for this <code>SuggestBox</code>
	 */
	public SuggestBox(SuggestOracle oracle) {
		this(oracle, new TextBox());
	}

	/**
	 * Constructor for {@link SuggestBox}. The text box will be removed from it's current location and wrapped
	 * by the {@link SuggestBox}.
	 *
	 * @param oracle supplies suggestions based upon the current contents of the text widget
	 * @param box the text widget
	 */
	public SuggestBox(SuggestOracle oracle, ValueBoxBase<String> box) {
		this(oracle, box, new DefaultSuggestionDisplay());
	}

	/**
	 * Constructor for {@link SuggestBox}. The text box will be removed from it's current location and wrapped
	 * by the {@link SuggestBox}.
	 *
	 * @param oracle supplies suggestions based upon the current contents of the text widget
	 * @param box the text widget
	 * @param suggestDisplay the class used to display suggestions
	 */
	public SuggestBox(SuggestOracle oracle, ValueBoxBase<String> box, SuggestionDisplay suggestDisplay) {
		super(oracle, box, suggestDisplay);
		setStyleName(Styles.FORM_CONTROL);
	}

}
