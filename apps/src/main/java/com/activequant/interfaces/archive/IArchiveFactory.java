package com.activequant.interfaces.archive;

import com.activequant.domainmodel.TimeFrame;

public interface IArchiveFactory {

    IArchiveReader getReader(TimeFrame tf);

    IArchiveWriter getWriter(TimeFrame tf);
}
