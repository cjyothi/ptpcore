package com.dms.ptp.response;

import java.util.List;

import com.dms.ptp.entity.Catalog;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatalogResponse {
	
	private int total;
	private List<Catalog> items;
	
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public List<Catalog> getItems() {
        return items;
    }
    public void setItems(List<Catalog> items) {
        this.items = items;
    }
	
	
}
