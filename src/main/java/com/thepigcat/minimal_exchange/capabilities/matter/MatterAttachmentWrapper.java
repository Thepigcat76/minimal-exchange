package com.thepigcat.minimal_exchange.capabilities.matter;

import com.thepigcat.minimal_exchange.data.MEAttachmentTypes;
import net.neoforged.neoforge.attachment.AttachmentHolder;

public class MatterAttachmentWrapper implements IMatterStorage{
    private final AttachmentHolder holder;
    private final int capacity;

    public MatterAttachmentWrapper(AttachmentHolder holder, int capacity) {
        this.holder = holder;
        this.capacity = capacity;
    }

    @Override
    public int getMatter() {
        return holder.getData(MEAttachmentTypes.MATTER);
    }

    @Override
    public int getMatterCapacity() {
        return this.capacity;
    }

    @Override
    public void setMatter(int matter) {
        holder.setData(MEAttachmentTypes.MATTER, matter);
    }

    @Override
    public void setMatterCapacity(int matterCapacity) {
    }

}
