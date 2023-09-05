package com.programmerartist.insecticide.web.controller.persistence;

/**
 * Created by 程序员Artist on 16/3/28.
 */
public interface Persist<D> {

    /**
     *
     * @param data
     * @throws Exception
     */
    void save(D data) throws Exception;

    /**
     *
     * @param data
     * @throws Exception
     */
    void update(D data) throws Exception;

    /**
     *
     * @param data
     * @throws Exception
     */
    void delete(D data) throws Exception;

    /**
     *
     * @throws Exception
     */
    void reload() throws Exception;
}
