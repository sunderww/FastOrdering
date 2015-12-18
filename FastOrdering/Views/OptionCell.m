//
//  OptionCell.m
//  FastOrdering
//
//  Created by Sunder on 06/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "OptionCell.h"

@implementation OptionCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
	[super setSelected:selected animated:animated];
	
	if (selected) {
		self.textField.text = @"";
		[self.textField becomeFirstResponder];
	}
}

- (void)setOption:(Option *)option andTag:(NSUInteger)tag {
	mainLabel.text = option.name;
	self.textField.text = @"0";
	self.textField.tag = tag;
}

- (void)setQuantity:(NSInteger)quantity {
	self.textField.text = [NSString stringWithFormat:@"%ld", (long)quantity];
}

@end
