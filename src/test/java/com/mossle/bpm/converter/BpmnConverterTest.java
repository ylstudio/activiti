package com.mossle.bpm.converter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.model.BpmnModel;

import com.mossle.bpm.converter.extend.node.MyUserTask;

/**
 * @author izerui.com
 *  bpmnModel--> xml  
 *  xml ---> bpmnModel 
 */
public class BpmnConverterTest {

	protected BpmnModel readXMLFile() throws Exception {
		InputStream xmlStream = this.getClass().getClassLoader().getResourceAsStream("diagrams/converter/multi-instance.bpmn20.xml");
		XMLInputFactory xif = XMLInputFactory.newInstance();
		InputStreamReader in = new InputStreamReader(xmlStream, "UTF-8");
		XMLStreamReader xtr = xif.createXMLStreamReader(in);
		BpmnModel bpmnModel = new MyBpmnXMLConverter().convertToBpmnModel(xtr);
		return bpmnModel;
	}

	protected BpmnModel exportAndReadXMLFile(BpmnModel bpmnModel) throws Exception {
		byte[] xml = new MyBpmnXMLConverter().convertToXML(bpmnModel);
		System.out.println("xml " + new String(xml, "UTF-8"));
		XMLInputFactory xif = XMLInputFactory.newInstance();
		InputStreamReader in = new InputStreamReader(new ByteArrayInputStream(xml), "UTF-8");
		XMLStreamReader xtr = xif.createXMLStreamReader(in);
		BpmnModel parsedModel = new MyBpmnXMLConverter().convertToBpmnModel(xtr);
		return parsedModel;
	}

	public static void main(String[] args) throws Exception {
		
		BpmnConverterTest t = new BpmnConverterTest();
		BpmnModel bpmnModel = t.readXMLFile();
		System.out.println(((MyUserTask)bpmnModel.getFlowElement("taskuser-1")).getXuhuisheng());
		System.out.println(((MyUserTask)bpmnModel.getFlowElement("taskuser-1")).getTests());
		t.exportAndReadXMLFile(bpmnModel);
	}
}
