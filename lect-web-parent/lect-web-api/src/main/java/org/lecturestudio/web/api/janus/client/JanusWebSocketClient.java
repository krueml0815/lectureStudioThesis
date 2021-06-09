/*
 * Copyright (C) 2021 TU Darmstadt, Department of Computer Science,
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

package org.lecturestudio.web.api.janus.client;

import static java.util.Objects.nonNull;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Builder;
import java.net.http.WebSocket.Listener;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletionStage;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;

import org.lecturestudio.core.ExecutableBase;
import org.lecturestudio.core.ExecutableException;
import org.lecturestudio.web.api.data.bind.JsonConfigProvider;
import org.lecturestudio.web.api.janus.JanusHandler;
import org.lecturestudio.web.api.janus.JanusMessageTransmitter;
import org.lecturestudio.web.api.janus.json.JanusMessageFactory;
import org.lecturestudio.web.api.janus.message.JanusEventType;
import org.lecturestudio.web.api.janus.message.JanusMessage;

public class JanusWebSocketClient extends ExecutableBase implements JanusMessageTransmitter {

	private WebSocket webSocket;

	private Jsonb jsonb;

	private JanusHandler handler;


	@Override
	public void sendMessage(JanusMessage message) {
		System.out.println("sending message:");
		System.out.println(jsonb.toJson(message));
		System.out.println();

		webSocket.sendText(jsonb.toJson(message), true)
				.exceptionally(throwable -> {
					logException(throwable, "Send Janus message failed");
					return null;
				});
	}

	@Override
	protected void initInternal() throws ExecutableException {
		jsonb = new JsonConfigProvider().getContext(null);
		handler = new JanusHandler(this);
	}

	@Override
	protected void startInternal() throws ExecutableException {
		HttpClient httpClient = HttpClient.newBuilder().build();

		Builder webSocketBuilder = httpClient.newWebSocketBuilder();
		webSocketBuilder.subprotocols("janus-protocol");
		webSocketBuilder.connectTimeout(Duration.of(10, ChronoUnit.SECONDS));

		webSocket = webSocketBuilder
				.buildAsync(URI.create("ws://127.0.0.1:8188"),
						new WebSocketListener())
				.exceptionally(throwable -> {
					return null;
				}).join();

		handler.start();
	}

	@Override
	protected void stopInternal() throws ExecutableException {
		webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "disconnect").join();
	}

	@Override
	protected void destroyInternal() throws ExecutableException {

	}



	private class WebSocketListener implements Listener {

		@Override
		public void onError(WebSocket webSocket, Throwable error) {
			System.out.println("Error occurred: " + error.getMessage());
			error.printStackTrace();

			Listener.super.onError(webSocket, error);
		}

		@Override
		public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
			System.out.println("onText: " + data);

			StringReader reader = new StringReader(data.toString());
			JsonObject body = Json.createReader(reader).readObject();
			JanusEventType type = JanusEventType.fromType(body.getString("janus"));

			if (nonNull(type)) {
				JanusMessage message = JanusMessageFactory.createMessage(body, type);

				try {
					handler.handleMessage(message);
				}
				catch (Exception e) {
					logException(e, "Process Janus message failed");
				}
			}
			else {
				logMessage("Non existing Janus event type received");
			}

			return Listener.super.onText(webSocket, data, last);
		}
	}
}
