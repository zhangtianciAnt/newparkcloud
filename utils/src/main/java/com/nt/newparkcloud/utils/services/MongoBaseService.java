package com.nt.newparkcloud.utils.services;

import com.nt.newparkcloud.utils.LogicalException;

import javax.servlet.http.HttpServletRequest;

public interface MongoBaseService {

    <T>T selectAllWithAuth(T record, HttpServletRequest request) throws LogicalException;
}
