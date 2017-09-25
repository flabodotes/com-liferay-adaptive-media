/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.adaptive.media.content.transformer;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileEntry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Adolfo Pérez
 */
public abstract class BaseRegexStringContentTransformer
	implements ContentTransformer<String> {

	@Override
	public abstract ContentTransformerContentType<String>
		getContentTransformerContentType();

	@Override
	public String transform(String content) throws PortalException {
		if (content == null) {
			return null;
		}

		Pattern pattern = getPattern();

		Matcher matcher = pattern.matcher(content);

		StringBuffer sb = new StringBuffer(content.length());

		while (matcher.find()) {
			FileEntry fileEntry = getFileEntry(matcher);

			String imgTag = matcher.group(0);

			String adaptiveTag = createImageTag(imgTag, fileEntry);

			matcher.appendReplacement(
				sb, Matcher.quoteReplacement(adaptiveTag));
		}

		matcher.appendTail(sb);

		return sb.toString();
	}

	protected abstract String createImageTag(
			String originalImgTag, FileEntry fileEntry)
		throws PortalException;

	protected abstract FileEntry getFileEntry(Matcher matcher)
		throws PortalException;

	protected abstract Pattern getPattern();

}