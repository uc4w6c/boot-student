@17
D=A
@256
M=D
@17
D=A
@257
M=D
@257
D=M
@256
D=D-M
@EQ_TRUE256
D;JEQ
(EQ_FALSE256)
@256
M=0
@EQ_END256
D;JMP
(EQ_TRUE256)
@256
M=-1
(EQ_END256)
@257
M=0
@17
D=A
@257
M=D
@16
D=A
@258
M=D
@258
D=M
@257
D=D-M
@EQ_TRUE257
D;JEQ
(EQ_FALSE257)
@257
M=0
@EQ_END257
D;JMP
(EQ_TRUE257)
@257
M=-1
(EQ_END257)
@258
M=0
@16
D=A
@258
M=D
@17
D=A
@259
M=D
@259
D=M
@258
D=D-M
@EQ_TRUE258
D;JEQ
(EQ_FALSE258)
@258
M=0
@EQ_END258
D;JMP
(EQ_TRUE258)
@258
M=-1
(EQ_END258)
@259
M=0
@892
D=A
@259
M=D
@891
D=A
@260
M=D
@260
D=M
@259
D=M-D
@LT_TRUE259
D;JLT
(LT_FALSE259)
@259
M=0
@LT_END259
D;JMP
(LT_TRUE259)
@259
M=-1
(LT_END259)
@260
M=0
@891
D=A
@260
M=D
@892
D=A
@261
M=D
@261
D=M
@260
D=M-D
@LT_TRUE260
D;JLT
(LT_FALSE260)
@260
M=0
@LT_END260
D;JMP
(LT_TRUE260)
@260
M=-1
(LT_END260)
@261
M=0
@891
D=A
@261
M=D
@891
D=A
@262
M=D
@262
D=M
@261
D=M-D
@LT_TRUE261
D;JLT
(LT_FALSE261)
@261
M=0
@LT_END261
D;JMP
(LT_TRUE261)
@261
M=-1
(LT_END261)
@262
M=0
@32767
D=A
@262
M=D
@32766
D=A
@263
M=D
@263
D=M
@262
D=M-D
@GT_TRUE262
D;JGT
(GT_FALSE262)
@262
M=0
@GT_END262
D;JMP
(GT_TRUE262)
@262
M=-1
(GT_END262)
@263
M=0
@32766
D=A
@263
M=D
@32767
D=A
@264
M=D
@264
D=M
@263
D=M-D
@GT_TRUE263
D;JGT
(GT_FALSE263)
@263
M=0
@GT_END263
D;JMP
(GT_TRUE263)
@263
M=-1
(GT_END263)
@264
M=0
@32766
D=A
@264
M=D
@32766
D=A
@265
M=D
@265
D=M
@264
D=M-D
@GT_TRUE264
D;JGT
(GT_FALSE264)
@264
M=0
@GT_END264
D;JMP
(GT_TRUE264)
@264
M=-1
(GT_END264)
@265
M=0
@57
D=A
@265
M=D
@31
D=A
@266
M=D
@53
D=A
@267
M=D
@267
D=M
@266
M=D+M
@267
M=0
@112
D=A
@267
M=D
@267
D=M
@266
M=M-D
@267
M=0
@266
M=-M
@266
D=M
@265
M=D&M
@266
M=0
@82
D=A
@266
M=D
@266
D=M
@265
M=D|M
@266
M=0
@265
M=!M
