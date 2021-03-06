package org.kie.remote.services.rest.query;

import static org.kie.remote.services.rest.query.RemoteServicesCriteriaUtil.*;
import static org.jbpm.query.jpa.data.QueryParameterIdentifiersUtil.getQueryParameterIdNameMap;
import static org.jbpm.services.task.persistence.TaskQueryCriteriaUtil.taskImplSpecificGetEntityField;
import static org.jbpm.services.task.persistence.TaskQueryCriteriaUtil.taskSpecificCreatePredicateFromSingleCriteria;
import static org.kie.remote.services.rest.query.RemoteServicesCriteriaUtil.getJoinRootClassAndAddNeededJoin;
import static org.kie.remote.services.rest.query.RemoteServicesQueryData.checkAndInitializeCriteriaAttributes;
import static org.kie.remote.services.rest.query.RemoteServicesQueryData.taskSpecificCriterias;
import static org.kie.remote.services.rest.query.RemoteServicesQueryData.varInstLogSpecificCriterias;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.Attribute;

import org.jbpm.process.audit.AuditQueryCriteriaUtil;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.VariableInstanceLog;
import org.jbpm.query.jpa.data.QueryCriteria;
import org.jbpm.query.jpa.data.QueryWhere;
import org.jbpm.services.task.impl.model.TaskImpl;

public class RemoteServicesAuditQueryCriteriaUtil extends AuditQueryCriteriaUtil {

    // Query Field Info -----------------------------------------------------------------------------------------------------------
    
    public final static Map<Class, Map<String, Attribute>> criteriaAttributes 
        = RemoteServicesQueryData.criteriaAttributes;

    @Override
    protected synchronized boolean initializeCriteriaAttributes() { 
        return RemoteServicesQueryData.initializeCriteriaAttributes();
    }
   
    // Implementation specific logic ----------------------------------------------------------------------------------------------
   
    public RemoteServicesAuditQueryCriteriaUtil(RemoteServicesQueryJPAService service) { 
        super(criteriaAttributes, service);
        checkAndInitializeCriteriaAttributes();
    }
  
    // Implementation specific methods --------------------------------------------------------------------------------------------
  
    @Override
    protected <R,T> Predicate implSpecificCreatePredicateFromSingleCriteria( 
            CriteriaQuery<R> query, 
            CriteriaBuilder builder, 
            Class queryType,
            QueryCriteria criteria, 
            QueryWhere queryWhere) {
     
        Class newJoinRootClass = getJoinRootClassAndAddNeededJoin(query, builder, queryType, criteria, queryWhere);
      
        Attribute attr = getCriteriaAttributes().get(newJoinRootClass).get(criteria.getListId());
        
        if( TaskImpl.class.equals(newJoinRootClass) ) { 
            return createJoinedTaskPredicateFromSingleCriteria(query, builder, criteria, attr);
        } else if( ProcessInstanceLog.class.equals(newJoinRootClass)
                || VariableInstanceLog.class.equals(newJoinRootClass) ) {
            return createJoinedAuditPredicateFromSingleCriteria(query, builder, criteria, attr); 
        } else {
            throw new IllegalStateException("Unexpected query type " + queryType.getSimpleName() + " when processing criteria " 
                    + getQueryParameterIdNameMap().get(Integer.parseInt(criteria.getListId())) + " (" + criteria.toString() + ")");
        }
    }

    @Override
    protected <T> List<T> createQueryAndCallApplyMetaCriteriaAndGetResult(QueryWhere queryWhere, CriteriaQuery<T> criteriaQuery, CriteriaBuilder builder) { 
        return sharedCreateQueryAndCallApplyMetaCriteriaAndGetResult(queryWhere, criteriaQuery, builder, (RemoteServicesQueryJPAService) jpaService);
    }
}
