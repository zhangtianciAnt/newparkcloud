package com.nt.service_Org.Impl;

import com.nt.dao_Org.Information;
import com.nt.service_Org.InformationService;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
public class InformationServiceImpl implements InformationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Information information,TokenModel tokenModel) throws Exception {
        if(information.getActivityinfo()!=null&&information.getActivityinfo().getSignupinfo()!=null)
        {
            information.getActivityinfo().preInsert(tokenModel);
            mongoTemplate.save(information);
        }
        else
        {
            if(information.getType().equals("8")||information.getType().equals("9")||information.getType().equals("10"))
            {
                Query query = new Query();
                query.addCriteria(Criteria.where("type").is(information.getType()));
                query.addCriteria(Criteria.where("releasestatus").is("1"));
                List<Information> rst = mongoTemplate.find(query,Information.class);
                if(rst.size() > 0){
                    rst.get(0).setReleasestatus("2");
                    mongoTemplate.save(rst.get(0));
                    mongoTemplate.save(information);
                }
                else
                {
                    mongoTemplate.save(information);
                }
            }
            else
            {
                mongoTemplate.save(information);
            }

        }
    }

    @Override
    public List<Information> get(Information information) throws Exception {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC,   "releasetime"));
        return mongoTemplate.find(query, Information.class);
    }
}
