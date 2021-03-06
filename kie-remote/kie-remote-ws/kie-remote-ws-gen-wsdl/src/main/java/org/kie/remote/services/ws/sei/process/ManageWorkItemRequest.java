/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.kie.remote.services.ws.sei.process;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import org.kie.remote.services.ws.sei.StringObjectEntryList;

/**
 * Only used for initial WSDL generation
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManageWorkItemRequest", propOrder = {
    "operation",
    "deploymentId",
    "workItemId",
    "results"
})
public class ManageWorkItemRequest {

    @XmlElement
    private WorkItemOperationType operation;
    
    @XmlElement(required=true)
    @XmlSchemaType(name="string")
    private String deploymentId;
    
    @XmlElement(required=true)
    @XmlSchemaType(name="long")
    private Long workItemId;

    @XmlElement
    private StringObjectEntryList results;
    
}
