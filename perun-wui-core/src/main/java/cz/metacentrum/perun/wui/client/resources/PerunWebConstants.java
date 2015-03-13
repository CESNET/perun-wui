package cz.metacentrum.perun.wui.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

/**
 * Interface to access common GUI settings like RPC URL, callback timeouts,...
 * Settings are stored in src/resources/{devel,production}/{package_path}/PerunWebConstants.properties
 *
 * @author Vaclav Mach <374430@mail.muni.cz>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunWebConstants extends Constants {

	public static final PerunWebConstants INSTANCE = GWT.create(PerunWebConstants.class);

	/**
	 * Return RPC URL modified for per-developer devel instances
	 *
	 * @return
	 */
	String perunRpcUrlModifier();

	/**
	 * Default interval for refreshing window with pending requests
	 *
	 * @return time in milliseconds
	 */
	int pendingRequestsRefreshInterval();

	/**
	 * Link in the footer of GUI
	 *
	 * @return
	 */
	String footerPerunLink();

	/**
	 * License text in footer
	 *
	 * @return
	 */
	String footerPerunLicense();

	/**
	 * Copyright text in footer
	 *
	 * @return
	 */
	String footerPerunCopyright();

	/**
	 * Copyright text in footer
	 *
	 * @return
	 */
	String guiVersion();

}