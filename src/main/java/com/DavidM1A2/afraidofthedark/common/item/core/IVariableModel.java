package com.DavidM1A2.afraidofthedark.common.item.core;

import java.util.Map;

/**
 * Interface to be implemented when an item has a model that changes based on metadata state
 */
public interface IVariableModel
{
    /**
     * Can be overridden to return a custom mapping of metadata -> model to be used by this item
     *
     * @return Mapping of metadata->model name to be used by this item
     */
    Map<Integer, String> getModelVariants();
}
