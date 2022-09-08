/**
 * Copyright (c) 2011 GitHub Inc. All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html Contributors: Kevin Sawicki (GitHub Inc.) - initial API and implementation
 */
package org.eclipse.egit.github.core;


import java.io.Serializable;

public class DownloadResourceProduct implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String accesskeyid;
	private String acl;
	private String mimeType;
	private String path;
	private String policy;
	private String s3Url;
	private String signature;

	public String getAccesskeyid() {
		return accesskeyid;
	}

	public String getAcl() {
		return acl;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getPath() {
		return path;
	}

	public String getPolicy() {
		return policy;
	}

	public String getS3Url() {
		return s3Url;
	}

	public String getSignature() {
		return signature;
	}

	/**
	* @param accesskeyid
	* @return  this download resource
	*/
	public DownloadResource setAccesskeyid(String accesskeyid, DownloadResource downloadResource) {
		this.accesskeyid = accesskeyid;
		return downloadResource;
	}

	/**
	* @param acl
	* @return  this download resource
	*/
	public DownloadResource setAcl(String acl, DownloadResource downloadResource) {
		this.acl = acl;
		return downloadResource;
	}

	/**
	* @param mimeType
	* @return  this download resource
	*/
	public DownloadResource setMimeType(String mimeType, DownloadResource downloadResource) {
		this.mimeType = mimeType;
		return downloadResource;
	}

	/**
	* @param path
	* @return  this download resource
	*/
	public DownloadResource setPath(String path, DownloadResource downloadResource) {
		this.path = path;
		return downloadResource;
	}

	/**
	* @param policy
	* @return  this download resource
	*/
	public DownloadResource setPolicy(String policy, DownloadResource downloadResource) {
		this.policy = policy;
		return downloadResource;
	}

	/**
	* @param s3Url
	* @return  this download resource
	*/
	public DownloadResource setS3Url(String s3Url, DownloadResource downloadResource) {
		this.s3Url = s3Url;
		return downloadResource;
	}

	/**
	* @param signature
	* @return  this download resource
	*/
	public DownloadResource setSignature(String signature, DownloadResource downloadResource) {
		this.signature = signature;
		return downloadResource;
	}
}