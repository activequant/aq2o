package com.activequant.archive;

import com.activequant.domainmodel.TimeFrame;

public interface IArchiveFactory {

    IArchiveReader getReader(TimeFrame tf);

    IArchiveWriter getWriter(TimeFrame tf);
}
