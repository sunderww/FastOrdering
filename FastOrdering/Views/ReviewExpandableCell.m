//
//  ReviewExpandableCell.m
//  FastOrdering
//
//  Created by Sunder on 08/04/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "ReviewExpandableCell.h"

@implementation ReviewExpandableCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setExpansionStyle:(UIExpansionStyle)expansionStyle animated:(BOOL)animated {
  if (expansionStyle != _expansionStyle) {
    _expansionStyle = expansionStyle;
    [self updateData];
  }
}

- (void)setText:(NSString *)text {
  _text = text;
  [self updateData];
}

- (void)updateData {
  self.textLabel.text = self.text;
}

@end
