package cz.metacentrum.perun.wui.registrar.pages.steps;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.html.Paragraph;

/**
 * Represents VO extension application form step. But show question if user want to do it.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class VoExtOfferStep extends VoExtStep {

	public VoExtOfferStep(RegistrarObject registrar, PerunForm form) {
		super(registrar, form);
	}

	@Override
	public void call(final PerunPrincipal pp, final Summary summary, final Events<Result> events) {

		PerunRegistrarTranslation translation = GWT.create(PerunRegistrarTranslation.class);

		final Modal modal = new Modal();
		modal.setTitle(translation.offerMembershipExtensionTitle());
		modal.setFade(true);
		modal.setDataKeyboard(false);
		modal.setDataBackdrop(ModalBackdrop.STATIC);
		modal.setClosable(false);

		ModalBody body = new ModalBody();
		body.add(new Paragraph(translation.offerMembershipExtensionMessage(registrar.getVo().getName())));


		ModalFooter footer = new ModalFooter();

		final Button noThanks = new Button(translation.offerMembershipExtensionNoThanks(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				modal.hide();
				events.onLoadingStart();
				events.onFinished(null);
			}
		});
		noThanks.setType(ButtonType.DEFAULT);

		final Button extend = new Button(translation.offerMembershipExtensionExtend(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				modal.hide();
				voExtStepCall(pp, summary, events);
			}
		});
		extend.setType(ButtonType.SUCCESS);
		footer.add(extend);
		footer.add(noThanks);

		modal.add(body);
		modal.add(footer);
		modal.show();

	}

	private void voExtStepCall(PerunPrincipal pp, Summary summary, Events<Result> events) {
		super.call(pp, summary, events);
	}
}
