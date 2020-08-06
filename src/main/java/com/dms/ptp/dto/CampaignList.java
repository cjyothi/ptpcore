package com.dms.ptp.dto;

import com.dms.ptp.entity.Campaign;

public class CampaignList {
	
    PageDecorator<Campaign> activeList;
    PageDecorator<Campaign> pendingList;
    PageDecorator<Campaign> postList;
    PageDecorator<Campaign> draftList;
    PageDecorator<Campaign> cancelledList;
    PageDecorator<Campaign> allList;
    PageDecorator<Campaign> campaignStatusList;

    public PageDecorator<Campaign> getActiveList() {
        return activeList;
    }

    public void setActiveList(PageDecorator<Campaign> activeList) {
        this.activeList = activeList;
    }

    public PageDecorator<Campaign> getPendingList() {
        return pendingList;
    }

    public void setPendingList(PageDecorator<Campaign> pendingList) {
        this.pendingList = pendingList;
    }

    public PageDecorator<Campaign> getPostList() {
        return postList;
    }

    public void setPostList(PageDecorator<Campaign> postList) {
        this.postList = postList;
    }

    public PageDecorator<Campaign> getDraftList() {
        return draftList;
    }

    public void setDraftList(PageDecorator<Campaign> draftList) {
        this.draftList = draftList;
    }

    public PageDecorator<Campaign> getCancelledList() {
        return cancelledList;
    }

    public void setCancelledList(PageDecorator<Campaign> cancelledList) {
        this.cancelledList = cancelledList;
    }

    public PageDecorator<Campaign> getAllList() {
        return allList;
    }

    public void setAllList(PageDecorator<Campaign> allList) {
        this.allList = allList;
    }

	public PageDecorator<Campaign> getCampaignStatusList() {
		return campaignStatusList;
	}

	public void setCampaignStatusList(PageDecorator<Campaign> campaignStatusList) {
		this.campaignStatusList = campaignStatusList;
	}
}
