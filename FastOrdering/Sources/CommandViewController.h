//
//  CommandViewController.h
//  FastOrdering
//
//  Created by Sunder on 04/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CommandViewController : UIViewController {
    IBOutlet UIButton *     menuButton;
    IBOutlet UIButton *     alacarteButton;
    IBOutlet UIButton *     reviewButton;
    
    IBOutletCollection(UIView) NSArray * clickedViews;
    IBOutletCollection(UIView) NSArray * contentViews;
}

- (IBAction)buttonClicked:(UIButton *)sender;

@end
