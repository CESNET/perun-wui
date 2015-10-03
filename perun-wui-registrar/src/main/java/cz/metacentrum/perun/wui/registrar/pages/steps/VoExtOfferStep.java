package cz.metacentrum.perun.wui.registrar.pages.steps;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.client.RegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;
import cz.metacentrum.perun.wui.registrar.pages.FormView;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.html.Paragraph;

/**
 * Created by ondrej on 3.10.15.
 */
public class VoExtOfferStep extends OfferStep {

	public VoExtOfferStep(FormView formView, Step yes, Step no) {
		super(formView, yes, no);
	}

	@Override
	public void call(final PerunPrincipal pp, final RegistrarObject registrar) {

		RegistrarTranslation translation = formView.getTranslation();

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
				no.call(pp, registrar);
			}
		});
		noThanks.setType(ButtonType.DEFAULT);

		final Button extend = new Button(translation.offerMembershipExtensionExtend(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				modal.hide();
				yes.call(pp, registrar);
			}
		});
		extend.setType(ButtonType.SUCCESS);
		footer.add(extend);
		footer.add(noThanks);

		modal.add(body);
		modal.add(footer);
		modal.show();


	}
}
