package com.activequant.servicelayer.soap;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.MTOM;
import javax.xml.ws.soap.SOAPBinding;

import com.activequant.dao.DaoException;
import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IInstrumentDao;
import com.activequant.domainmodel.Instrument;




@WebService(endpointInterface = "com.activequant.servicelayer.soap.IMainService", 
serviceName = "MainService")
@BindingType(SOAPBinding.SOAP11HTTP_BINDING)
@MTOM(enabled=false)
public class MainService implements IMainService {

    private IInstrumentDao idao;

    public MainService(IDaoFactory daoFactory) {
        this.idao = daoFactory.instrumentDao();
    }

    public String[] instrumentKeys() {
        try {
            return idao.loadIDs();
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Instrument loadInstrument(String primaryKey) {
        try {
            return idao.load(primaryKey);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int instrumentCount() {
        return idao.count();
    }

    public int mdiCount() {
        return (int) (100 * Math.random());
    }

}
