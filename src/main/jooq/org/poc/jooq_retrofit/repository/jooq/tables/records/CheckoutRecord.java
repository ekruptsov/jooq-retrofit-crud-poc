/*
 * This file is generated by jOOQ.
 */
package org.poc.jooq_retrofit.repository.jooq.tables.records;


import java.time.OffsetDateTime;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;
import org.poc.jooq_retrofit.repository.jooq.tables.Checkout;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CheckoutRecord extends UpdatableRecordImpl<CheckoutRecord> implements Record5<UUID, String, String, OffsetDateTime, OffsetDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.checkout.id</code>.
     */
    public CheckoutRecord setId(UUID value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.checkout.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.checkout.description</code>.
     */
    public CheckoutRecord setDescription(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.checkout.description</code>.
     */
    public String getDescription() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.checkout.status</code>.
     */
    public CheckoutRecord setStatus(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.checkout.status</code>.
     */
    public String getStatus() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.checkout.created</code>.
     */
    public CheckoutRecord setCreated(OffsetDateTime value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.checkout.created</code>.
     */
    public OffsetDateTime getCreated() {
        return (OffsetDateTime) get(3);
    }

    /**
     * Setter for <code>public.checkout.updated</code>.
     */
    public CheckoutRecord setUpdated(OffsetDateTime value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.checkout.updated</code>.
     */
    public OffsetDateTime getUpdated() {
        return (OffsetDateTime) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<UUID, String, String, OffsetDateTime, OffsetDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<UUID, String, String, OffsetDateTime, OffsetDateTime> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return Checkout.CHECKOUT.ID;
    }

    @Override
    public Field<String> field2() {
        return Checkout.CHECKOUT.DESCRIPTION;
    }

    @Override
    public Field<String> field3() {
        return Checkout.CHECKOUT.STATUS;
    }

    @Override
    public Field<OffsetDateTime> field4() {
        return Checkout.CHECKOUT.CREATED;
    }

    @Override
    public Field<OffsetDateTime> field5() {
        return Checkout.CHECKOUT.UPDATED;
    }

    @Override
    public UUID component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getDescription();
    }

    @Override
    public String component3() {
        return getStatus();
    }

    @Override
    public OffsetDateTime component4() {
        return getCreated();
    }

    @Override
    public OffsetDateTime component5() {
        return getUpdated();
    }

    @Override
    public UUID value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getDescription();
    }

    @Override
    public String value3() {
        return getStatus();
    }

    @Override
    public OffsetDateTime value4() {
        return getCreated();
    }

    @Override
    public OffsetDateTime value5() {
        return getUpdated();
    }

    @Override
    public CheckoutRecord value1(UUID value) {
        setId(value);
        return this;
    }

    @Override
    public CheckoutRecord value2(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public CheckoutRecord value3(String value) {
        setStatus(value);
        return this;
    }

    @Override
    public CheckoutRecord value4(OffsetDateTime value) {
        setCreated(value);
        return this;
    }

    @Override
    public CheckoutRecord value5(OffsetDateTime value) {
        setUpdated(value);
        return this;
    }

    @Override
    public CheckoutRecord values(UUID value1, String value2, String value3, OffsetDateTime value4, OffsetDateTime value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached CheckoutRecord
     */
    public CheckoutRecord() {
        super(Checkout.CHECKOUT);
    }

    /**
     * Create a detached, initialised CheckoutRecord
     */
    public CheckoutRecord(UUID id, String description, String status, OffsetDateTime created, OffsetDateTime updated) {
        super(Checkout.CHECKOUT);

        setId(id);
        setDescription(description);
        setStatus(status);
        setCreated(created);
        setUpdated(updated);
    }

    /**
     * Create a detached, initialised CheckoutRecord
     */
    public CheckoutRecord(org.poc.jooq_retrofit.repository.jooq.tables.pojos.Checkout value) {
        super(Checkout.CHECKOUT);

        if (value != null) {
            setId(value.getId());
            setDescription(value.getDescription());
            setStatus(value.getStatus());
            setCreated(value.getCreated());
            setUpdated(value.getUpdated());
        }
    }
}
