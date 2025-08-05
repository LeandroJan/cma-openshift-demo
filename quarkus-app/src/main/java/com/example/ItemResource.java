package com.example;

import com.example.model.Item;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    @GET
    public List<Item> list() {
        return Item.listAll();
    }

    @POST
    @Transactional
    public Item create(Item item) {
        // Clear the ID to ensure this is treated as a new entity
        // This prevents the "detached entity passed to persist" error
        // when clients accidentally include an ID in the JSON
        item.id = null;
        item.persist();
        return item;
    }

    @GET
    @Path("{id}")
    public Item get(@PathParam("id") Long id) {
        return Item.findById(id);
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        boolean deleted = Item.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Item not found");
        }
    }
}
