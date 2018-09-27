package com.nt.newparkcloud.utils.services;

import com.nt.newparkcloud.utils.LogicalException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MongoBaseService<T> {

    List<T> selectAllWithAuth(T record, HttpServletRequest request) throws LogicalException;
}
