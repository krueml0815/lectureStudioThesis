/*
 * Copyright (C) 2020 TU Darmstadt, Department of Computer Science,
 * Embedded Systems and Applications Group.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.lecturestudio.web.api.webrtc.config;

import dev.onvoid.webrtc.RTCBundlePolicy;
import dev.onvoid.webrtc.RTCIceServer;
import dev.onvoid.webrtc.RTCIceTransportPolicy;

public class DefaultConfiguration extends Configuration {

	public DefaultConfiguration() {
		getAudioConfiguration().setReceiveAudio(true);
		getAudioConfiguration().setSendAudio(true);

		getVideoConfiguration().setReceiveVideo(true);
		getVideoConfiguration().setSendVideo(true);

		getDesktopCaptureConfiguration().setFrameRate(15);

		RTCIceServer iceServer = new RTCIceServer();
		iceServer.urls.add("stun:stun.l.google.com:19302");

		getRTCConfig().iceTransportPolicy = RTCIceTransportPolicy.ALL;
		getRTCConfig().bundlePolicy = RTCBundlePolicy.MAX_BUNDLE;
		getRTCConfig().iceServers.add(iceServer);
	}

}
