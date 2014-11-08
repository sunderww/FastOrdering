//
//  TablePlanViewController.h
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TablePlanViewController : UIViewController {
    IBOutlet UIScrollView *  scrollView;
    IBOutlet UIButton *      backButton;
}

- (IBAction)goBack;

@end
