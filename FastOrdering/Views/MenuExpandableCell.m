//
//  MenuExpandableCell.m
//  FastOrdering
//
//  Created by Sunder on 21/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "MenuExpandableCell.h"

@implementation MenuExpandableCell

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

- (void)setMenu:(Menu *)menu {
  _menu = menu;
  [self updateData];
}

- (void)updateData {
  self.textLabel.text = self.menu.name;
}

@end
