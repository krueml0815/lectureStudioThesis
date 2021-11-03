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

package org.lecturestudio.presenter.api.view;

import org.lecturestudio.core.beans.ObjectProperty;
import org.lecturestudio.core.beans.StringProperty;
import org.lecturestudio.core.camera.AspectRatio;
import org.lecturestudio.core.camera.Camera;
import org.lecturestudio.core.camera.CameraFormat;
import org.lecturestudio.core.geometry.Rectangle2D;
import org.lecturestudio.core.view.ConsumerAction;

public interface CameraSettingsView extends SettingsBaseView {

	void setCameraName(StringProperty cameraName);

	void setCameraNames(String[] cameraNames);

	void setCameraFormats(CameraFormat[] cameraFormats);

	void setCameraViewRect(ObjectProperty<Rectangle2D> viewRect);

	void setCameraAspectRatio(AspectRatio ratio);

	void setCameraAspectRatios(AspectRatio[] ratios);

	void setOnCameraAspectRatioChanged(ConsumerAction<AspectRatio> action);

	void setOnViewVisible(ConsumerAction<Boolean> action);

	void setCamera(Camera camera);

	void setCameraFormat(CameraFormat cameraFormat);

	void setCameraError(String errorMessage);

	void startCameraPreview();

	void stopCameraPreview();

}
