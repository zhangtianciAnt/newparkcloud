package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.EvectionVo;
import com.nt.dao_Pfans.PFANS1000.Vo.TravelCostVo;
import com.nt.service_pfans.PFANS1000.EvectionService;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor=Exception.class)
public class EvectionServiceImpl implements EvectionService {

    @Autowired
    private EvectionMapper evectionMapper;
    @Autowired
    private TrafficDetailsMapper trafficdetailsMapper;
    @Autowired
    private AccommodationDetailsMapper accommodationdetailsMapper;
    @Autowired
    private OtherDetailsMapper otherdetailsMapper;

    @Autowired
    private InvoiceMapper invoicemapper;
    @Autowired
    private CurrencyexchangeMapper currencyexchangeMapper;

    @Autowired
    private TravelCostMapper travelcostmapper;

    @Override
    public List<Evection> get(Evection evection) throws Exception {
        return evectionMapper.select(evection);
    }

    @Override
    public  List<TravelCost> gettravelcost(TravelCostVo travelcostvo) throws Exception {
        List<TravelCost> Listvo = new ArrayList<TravelCost>();
        TravelCost travelcost = new TravelCost();
        List<TravelCost> travelcostlist = travelcostvo.getTravelcost();
        for(TravelCost travelList:travelcostlist){
//            travelcost.setPublicexpenseid(travelList.getPublicexpenseid());
            List<TravelCost> ListVo = travelcostmapper.select(travelcost);
            ListVo = ListVo.stream().sorted(Comparator.comparing(TravelCost::getNumber)).collect(Collectors.toList());
            Listvo.addAll(0,ListVo);
        }
        return Listvo;
    }

    @Override
    public EvectionVo selectById(String evectionid) throws Exception {
        EvectionVo eveVo = new EvectionVo();
        TrafficDetails trafficdetails = new TrafficDetails();
        AccommodationDetails accommodationdetails = new AccommodationDetails();
        OtherDetails otherdetails = new OtherDetails();
        Invoice invoice=new Invoice();
        Currencyexchange currencyexchange=new Currencyexchange();
        trafficdetails.setEvectionid(evectionid);
        accommodationdetails.setEvectionid(evectionid);
        otherdetails.setEvectionid(evectionid);
        invoice.setEvectionid(evectionid);
        currencyexchange.setEvectionid(evectionid);
        List<TrafficDetails> trafficdetailslist = trafficdetailsMapper.select(trafficdetails);
        List<AccommodationDetails> accommodationdetailslist = accommodationdetailsMapper.select(accommodationdetails);
        List<OtherDetails> otherdetailslist = otherdetailsMapper.select(otherdetails);
        List<Invoice> invoicelist=invoicemapper.select(invoice);
        List<Currencyexchange> currencyexchangeList = currencyexchangeMapper.select(currencyexchange);
        trafficdetailslist = trafficdetailslist.stream().sorted(Comparator.comparing(TrafficDetails::getRowindex)).collect(Collectors.toList());
        accommodationdetailslist = accommodationdetailslist.stream().sorted(Comparator.comparing(AccommodationDetails::getRowindex)).collect(Collectors.toList());
        otherdetailslist = otherdetailslist.stream().sorted(Comparator.comparing(OtherDetails::getRowindex)).collect(Collectors.toList());
        invoicelist=invoicelist.stream().sorted(Comparator.comparing(Invoice::getInvoicenumber)).collect(Collectors.toList());
        currencyexchangeList = currencyexchangeList.stream().sorted(Comparator.comparing(Currencyexchange::getRowindex)).collect(Collectors.toList());
        Evection Eve = evectionMapper.selectByPrimaryKey(evectionid);
        eveVo.setEvection(Eve);
        eveVo.setTrafficdetails(trafficdetailslist);
        eveVo.setAccommodationdetails(accommodationdetailslist);
        eveVo.setOtherdetails(otherdetailslist);
        eveVo.setInvoice(invoicelist);
        eveVo.setCurrencyexchanges(currencyexchangeList);
        return eveVo;
    }

    @Override
    public void updateEvectionVo(EvectionVo evectionVo, TokenModel tokenModel) throws Exception {
        Evection evection = new Evection();
        BeanUtils.copyProperties(evectionVo.getEvection(), evection);
        evection.preUpdate(tokenModel);
        evectionMapper.updateByPrimaryKey(evection);
        String evectionid = evection.getEvectionid();

        TrafficDetails traffic = new TrafficDetails();
        traffic.setEvectionid(evectionid);
        trafficdetailsMapper.delete(traffic);
        List<TrafficDetails> trafficdetailslist = evectionVo.getTrafficdetails();

        AccommodationDetails accommodation = new AccommodationDetails();
        accommodation.setEvectionid(evectionid);
        accommodationdetailsMapper.delete(accommodation);
        List<AccommodationDetails> accommodationdetailslist = evectionVo.getAccommodationdetails();

        OtherDetails other = new OtherDetails();
        other.setEvectionid(evectionid);
        otherdetailsMapper.delete(other);
        List<OtherDetails> otherdetailslist = evectionVo.getOtherdetails();

        Invoice invoice=new Invoice();
        invoice.setEvectionid(evectionid);
        invoicemapper.delete(invoice);
        List<Invoice> invoicelist=evectionVo.getInvoice();

        Currencyexchange currencyexchange = new Currencyexchange();
        currencyexchange.setEvectionid(evectionid);
        currencyexchangeMapper.delete(currencyexchange);
        List<Currencyexchange> currencyexchangeList = evectionVo.getCurrencyexchanges();

        if (trafficdetailslist != null) {
            int rowindex = 0;
            for (TrafficDetails trafficdetails : trafficdetailslist) {
                rowindex = rowindex + 1;
                trafficdetails.preInsert(tokenModel);
                trafficdetails.setTrafficdetails_id(UUID.randomUUID().toString());
                trafficdetails.setEvectionid(evectionid);
                trafficdetails.setRowindex(rowindex);
                trafficdetailsMapper.insertSelective(trafficdetails);
            }
        }
        if (accommodationdetailslist != null) {
            int rowindex = 0;
            for (AccommodationDetails accommodationdetails : accommodationdetailslist) {
                rowindex = rowindex + 1;
                accommodationdetails.preInsert(tokenModel);
                accommodationdetails.setAccommodationdetails_id(UUID.randomUUID().toString());
                accommodationdetails.setEvectionid(evectionid);
                accommodationdetails.setRowindex(rowindex);
                accommodationdetailsMapper.insertSelective(accommodationdetails);
            }
        }
        if (otherdetailslist != null) {
            int rowindex = 0;
            for (OtherDetails otherdetails : otherdetailslist) {
                rowindex = rowindex + 1;
                otherdetails.preInsert(tokenModel);
                otherdetails.setOtherdetails_id(UUID.randomUUID().toString());
                otherdetails.setEvectionid(evectionid);
                otherdetails.setRowindex(rowindex);
                otherdetailsMapper.insertSelective(otherdetails);

            }
        }
        if (invoicelist != null) {
            int rowundex = 0;
            for (Invoice invoicel : invoicelist) {
                rowundex = rowundex + 1;
                invoicel.preInsert(tokenModel);
                invoicel.setInvoice_id(UUID.randomUUID().toString());
                invoicel.setEvectionid(evectionid);
                invoicel.setRowindex(rowundex);
                invoicemapper.insertSelective(invoicel);
            }
        }
        if (currencyexchangeList != null) {
            int rowundex = 0;
            for (Currencyexchange curr : currencyexchangeList) {
                rowundex = rowundex + 1;
                curr.preInsert(tokenModel);
                curr.setCurrencyexchangeid(UUID.randomUUID().toString());
                curr.setEvectionid(evectionid);
                curr.setRowindex(rowundex);
                currencyexchangeMapper.insertSelective(curr);
            }
        }

    }

    @Override
    public void insertEvectionVo(EvectionVo evectionVo, TokenModel tokenModel) throws Exception {
        String evectionid = UUID.randomUUID().toString();
        Evection evection = new Evection();
        BeanUtils.copyProperties(evectionVo.getEvection(), evection);
        evection.preInsert(tokenModel);
        evection.setEvectionid(evectionid);
        evectionMapper.insertSelective(evection);
        List<TrafficDetails> trafficdetailslist = evectionVo.getTrafficdetails();
        List<AccommodationDetails> accommodationdetailslist = evectionVo.getAccommodationdetails();
        List<OtherDetails> otherdetailslist = evectionVo.getOtherdetails();
        List<Invoice> invoicelist=evectionVo.getInvoice();
        List<Currencyexchange> currencyexchangeList = evectionVo.getCurrencyexchanges();

        if (trafficdetailslist != null) {
            int rowindex = 0;
            for (TrafficDetails trafficdetails : trafficdetailslist) {
                rowindex = rowindex + 1;
                trafficdetails.preInsert(tokenModel);
                trafficdetails.setTrafficdetails_id(UUID.randomUUID().toString());
                trafficdetails.setEvectionid(evectionid);
                trafficdetails.setRowindex(rowindex);
                trafficdetailsMapper.insertSelective(trafficdetails);
            }
        }

        if (accommodationdetailslist != null) {
            int rowindex = 0;
            for (AccommodationDetails accommodationdetails : accommodationdetailslist) {
                rowindex = rowindex + 1;
                accommodationdetails.preInsert(tokenModel);
                accommodationdetails.setAccommodationdetails_id(UUID.randomUUID().toString());
                accommodationdetails.setEvectionid(evectionid);
                accommodationdetails.setRowindex(rowindex);
                accommodationdetailsMapper.insertSelective(accommodationdetails);
            }
        }

        if (otherdetailslist != null) {
            int rowindex = 0;
            for (OtherDetails otherdetails : otherdetailslist) {
                rowindex = rowindex + 1;
                otherdetails.preInsert(tokenModel);
                otherdetails.setOtherdetails_id(UUID.randomUUID().toString());
                otherdetails.setEvectionid(evectionid);
                otherdetails.setRowindex(rowindex);
                otherdetailsMapper.insertSelective(otherdetails);
            }
        }

        if (invoicelist != null) {
            int rowundex = 0;
            for (Invoice invoice : invoicelist) {
                rowundex = rowundex + 1;
                invoice.preInsert(tokenModel);
                invoice.setInvoice_id(UUID.randomUUID().toString());
                invoice.setEvectionid(evectionid);
                invoice.setRowindex(rowundex);
                invoicemapper.insertSelective(invoice);
            }
        }
        if (currencyexchangeList != null) {
            int rowundex = 0;
            for (Currencyexchange curr : currencyexchangeList) {
                rowundex = rowundex + 1;
                curr.preInsert(tokenModel);
                curr.setCurrencyexchangeid(UUID.randomUUID().toString());
                curr.setEvectionid(evectionid);
                curr.setRowindex(rowundex);
                currencyexchangeMapper.insertSelective(curr);
            }
        }

    }

}
