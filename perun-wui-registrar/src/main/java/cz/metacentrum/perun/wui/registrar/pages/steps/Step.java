package cz.metacentrum.perun.wui.registrar.pages.steps;

import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;

/**
 * Created by ondrej on 3.10.15.
 */
public interface Step {
	void call(PerunPrincipal pp, RegistrarObject registrar);
}
