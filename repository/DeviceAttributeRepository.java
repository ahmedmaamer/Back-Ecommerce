package com.example.iotDataGenerator.repository;
import com.example.iotDataGenerator.entities.Attribute;
import com.example.iotDataGenerator.entities.Device;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

public class DeviceAttributeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getDeviceAttributes() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

        Root<Device> deviceRoot = criteriaQuery.from(Device.class);
        Join<Device, Attribute> attributeJoin = deviceRoot.join("attributes");

        criteriaQuery.multiselect(
                deviceRoot.get("deviceName"),
                deviceRoot.get("deviceType"),
                attributeJoin.get("attributeName"),
                attributeJoin.get("dataType"),
                attributeJoin.get("unit"),
                attributeJoin.get("attributeValue")
        );

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
