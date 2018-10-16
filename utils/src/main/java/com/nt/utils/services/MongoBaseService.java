package com.nt.utils.services;

import com.nt.utils.LogicalException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MongoBaseService<T> {

    List<T> selectAllWithAuth(T record, HttpServletRequest request) throws LogicalException;
}
